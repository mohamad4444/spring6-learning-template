#!/usr/bin/env python3
"""Run against a dev server: python3 scripts/smoke.py [http://localhost:8080]."""
import base64
import http.cookiejar
import json
import os
import sys
import urllib.error
import urllib.request

base = (sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8080").rstrip("/")
username = os.environ.get("SMOKE_USERNAME", "ester.nutzer@hs-kl.de")
password = os.environ.get("SMOKE_PASSWORD", "user1-secret")
auth = "Basic " + base64.b64encode(f"{username}:{password}".encode()).decode()
client = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))

def request(path, method="GET", body=None, csrf=None):
    headers = {"Authorization": auth, "Accept": "application/json"}
    if body is not None:
        headers["Content-Type"] = "application/json"
    if csrf:
        headers[csrf["headerName"]] = csrf["token"]
    req = urllib.request.Request(base + path, data=json.dumps(body).encode() if body is not None else None,
                                 headers=headers, method=method)
    with client.open(req, timeout=10) as response:
        data = response.read()
        return response.status, json.loads(data) if data else None

assert request("/actuator/health")[1]["status"] == "UP"
csrf = request("/api/v1/csrf")[1]
status, task = request("/api/v1/tasks", "POST", {"title": "Smoke test", "description": "Temporary task"}, csrf)
assert status == 201
path = "/api/v1/tasks/" + task["id"]
try:
    assert request(path)[1]["title"] == "Smoke test"
    status, updated = request(path, "PUT", {"title": "Smoke test", "description": "Verified", "status": "DONE", "version": task["version"]}, csrf)
    assert status == 200 and updated["status"] == "DONE" and updated["version"] > task["version"]
finally:
    assert request(path, "DELETE", csrf=csrf)[0] == 204
print("Smoke test passed: health, CSRF, authentication, create, read, update, delete")

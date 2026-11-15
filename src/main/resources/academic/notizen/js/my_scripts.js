"use strict";

// The original browser exercise now uses this application's origin and session.
let currentUser = null;
const element = (id) => document.getElementById(id);

window.addEventListener("DOMContentLoaded", () => {
  const actions = {
    loginBtn: login,
    logoutBtn: logout,
    registerBtn: createBenutzer,
    deleteBtn: deleteBenutzer,
    updateBtn: updateBenutzer,
    alleBenutzerBtn: getAlleBenutzer,
  };
  for (const [id, action] of Object.entries(actions)) {
    element(id).addEventListener("click", async () => {
      element("error").textContent = "";
      try {
        await action();
      } catch (error) {
        element("error").textContent = error.message;
      }
    });
  }
  showLoginData();
});

async function request(path, method = "GET", body, includeToken = true) {
  const headers = { Accept: "application/json" };
  if (body !== undefined) headers["Content-Type"] = "application/json";
  if (includeToken && currentUser) {
    headers.accessToken = currentUser.credential.accessToken;
  }
  if (!["GET", "HEAD"].includes(method)) {
    const response = await fetch("/api/v1/csrf", {
      credentials: "same-origin",
      headers: { Accept: "application/json" },
    });
    if (!response.ok || response.redirected) {
      throw new Error("Bitte zuerst bei Spring anmelden und die Seite neu laden.");
    }
    const csrf = await response.json();
    headers[csrf.headerName] = csrf.token;
  }
  const response = await fetch(path, {
    method,
    headers,
    credentials: "same-origin",
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  if (response.redirected) {
    throw new Error("Die Spring-Sitzung ist abgelaufen. Bitte erneut anmelden.");
  }
  const text = await response.text();
  let result = null;
  if (text) {
    try {
      result = JSON.parse(text);
    } catch {
      throw new Error("Unerwartete Antwort. Bitte die Spring-Anmeldung prüfen.");
    }
  }
  if (!response.ok) {
    throw new Error(result?.detail || result?.message || `Anfrage fehlgeschlagen (${response.status}).`);
  }
  return result;
}

function requireUser() {
  if (!currentUser) throw new Error("Bitte zuerst einen Übungsbenutzer anmelden oder registrieren.");
  return currentUser.id;
}

function showLoginData() {
  element("outBenutzerId").textContent = currentUser?.id ?? "Nicht angemeldet";
  element("outBenutzername").textContent = currentUser?.benutzername ?? "Nicht angemeldet";
  element("outAccessToken").textContent = currentUser ? JSON.stringify(currentUser.credential) : "Nicht angemeldet";
}

function clearInputs() {
  for (const input of document.querySelectorAll("input")) input.value = "";
  element("outAlleBenutzer").textContent = "";
}

async function login() {
  currentUser = await request("/access", "POST", {
    benutzername: element("loginBenutzername").value,
    password: element("loginPassword").value,
  }, false);
  clearInputs();
  showLoginData();
}

async function createBenutzer() {
  currentUser = await request("/benutzer", "POST", {
    vorname: element("regVorname").value,
    nachname: element("regNachname").value,
    benutzername: element("regBenutzername").value,
    password: element("regPassword").value,
  }, false);
  clearInputs();
  showLoginData();
}

async function logout() {
  requireUser();
  await request("/access", "DELETE");
  currentUser = null;
  clearInputs();
  showLoginData();
}

async function deleteBenutzer() {
  await request(`/benutzer/${requireUser()}`, "DELETE");
  currentUser = null;
  clearInputs();
  showLoginData();
}

async function updateBenutzer() {
  await request(`/benutzer/${requireUser()}`, "PUT", {
    vorname: element("updateVorname").value,
    nachname: element("updateNachname").value,
    password: element("updatePassword").value,
  });
  clearInputs();
  showLoginData();
}

async function getAlleBenutzer() {
  requireUser();
  const users = await request("/benutzer");
  element("outAlleBenutzer").textContent = JSON.stringify(users, null, 2);
}

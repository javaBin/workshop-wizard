import React from "react";

const LogoutButton = () => {
  return (
    <a href="/api/auth/logout">
      <button>Log out</button>
    </a>
  );
};

export default LogoutButton;

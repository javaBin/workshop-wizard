"use client";
import React from "react";
import LogoutButton from "@/components/LogoutButton";
import LoginButton from "@/components/LoginButton";
import { useUser } from "@auth0/nextjs-auth0/client";
import Workshops from "@/components/Workshops";

const Profile = () => {
  const { user, error, isLoading } = useUser();

  if (isLoading) {
    return <div>Loading ...</div>;
  }

  let content = null;

  if (user) {
    content = (
      <div>
        <img src={user.picture!} alt={user.name!} />
        <h2>{user.name}</h2>
        <p>{user.email}</p>
        <LogoutButton />
        <Workshops />
      </div>
    );
  } else {
    content = (
      <div>
        <LoginButton />
      </div>
    );
  }

  return <>{content}</>;
};

export default Profile;

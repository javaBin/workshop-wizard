import styles from "./page.module.css";
import Profile from "@/components/Profile";

export default function Home() {
  return (
    <main>
      <div className="flex justify-center items-center">
        <Profile />
      </div>
    </main>
  );
}

import React, { useState, useEffect } from "react";

type Workshop = {
  id: number;
  title: string;
  description: string;
};
const Workshops = () => {
  const [workshops, setWorkshops] = useState<Workshop[]>([]);

  useEffect(() => {
    fetch("/api/backend/workshop") // replace with your actual API endpoint
      .then((response) => response.json())
      .then((data) => setWorkshops(data));
  }, []);

  return (
    <div>
      <h1>Workshops</h1>
      {workshops.map((workshop) => (
        <div key={workshop.id}>
          <h2>{workshop.title}</h2>
          <p>{workshop.description}</p>
        </div>
      ))}
    </div>
  );
};

export default Workshops;

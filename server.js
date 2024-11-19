const express = require('express');
const app = express();
const port = 3000;

app.use(express.json());

let damagesData = [
    {
        id: 1,
        materialId: "M001",
        materialName: "Microscope",
        dateBorrowed: "2023-01-15",
        qty: 2,
        group: "A",
        yearSec: "3rd Year",
        instructor: "Dr. Smith",
        photo: "microscope.jpg",
        accountable: "John Doe",
        status: "Damaged"
    },
    // Add more sample data as needed
];

// Define routes
app.get('/damages', (req, res) => {
    res.json(damagesData);
});

app.post('/damages', (req, res) => {
    const newDamage = req.body;
    newDamage.id = damagesData.length + 1;
    damagesData.push(newDamage);
    res.status(201).json(newDamage);
});

app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});
const express = require('express');
const app = express();
const mysql = require('mysql');
const bodyParser = require('body-parser');

const connection = mysql.createConnection({
	host : 'localhost',
	user : 'root',
	password : 'password',
	database : 'contacts_db'
});

connection.connect();
connection.on('error', (err) => {
	console.log(err);
});

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

app.get('/', (req,res) => {
	connection.query('select number from numbers', (error,results,fields) => {
		if(error) res.send('Server error');
		res.send(results);
	});
});

app.post('/register', (req,res) => {
	connection.query(`insert into numbers(number) values(${req.body.number})`, (err,results) => {
		if(err) {
			if(err.errno == 1062) {
				res.status(409);
				res.send('Number already registered');
			} else {
				res.status(500);
				res.send('Could not register');
			}
		} else {
			res.status(201);
			res.send('Number registered');
		}
	});
});

app.post('/deregister', (req,res) => {
	connection.query(`delete from numbers where number=${req.body.number}`, (err,results) => {
		if(err) {
			res.status(500);
			res.send('Could not deregister');
		} else {
			if(results.affectedRows == 0) {
				res.status(404);
				res.send('Number not found');
			} else {
				res.status(200);
				res.send('Number deregistered');
			}
		}
	});
});

app.listen(3000, () => console.log('Listening on port 3000'));

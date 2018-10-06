const express = require('express');
const router = express.Router();
var app = express();

router.get('/', (req, res, next) => {
    res.status(200).json({
        message: 'Hello ' + req.param('name') + '!'
    });
    console.log(req.params);
});

app.use('/', router);
app.listen(3000);
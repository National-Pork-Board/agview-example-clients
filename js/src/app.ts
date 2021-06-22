import parse, * as csvParse from 'csv-parse';
import * as fs from 'fs';
import * as path from 'path'

var myParser: csvParse.Parser = parse({ delimiter: ',' }, function (err, data) {
    var rowNum = 0
    var headerRow: Array<string> = []
    for (let row of data) {
        if (rowNum++ == 0) {
            headerRow = row;
            console.log("Header: " + headerRow);
            continue
        }
        /*else {
            for (let i = 0; i <)
                console.log(headerRow[0] + " = " + row[0]);
        }*/
        console.log("Row: " + row);
        for (let value of row) {
            console.log("Value: " + value)
        }
    }
}) as csvParse.Parser;

const filePath = path.join(__dirname, '../../db/movement_addresses.csv');
fs.createReadStream(filePath).pipe(myParser);

import parse, * as csvParse from 'csv-parse';
import * as fs from 'fs';
import * as path from 'path'

export function getData(movementFilePath: string, movementAddressPath: string) {

    var myParser: csvParse.Parser = parse({ delimiter: ',' }, function (err, data) {
    }) as csvParse.Parser;

    const filePath = path.join(__dirname, movementAddressPath);
    let stream = fs.createReadStream(filePath)
    stream.pipe(myParser);

    const chunks: Array<Array<string>> = [];
    return new Promise(function (resolve, reject) {
        myParser.on("data", (chunk) => chunks.push(chunk))
        myParser.on("end", () => resolve(chunks))
        myParser.on("error", (err) => reject(err));
    })
}

/*var myParser: csvParse.Parser = parse({ delimiter: ',' }, function (err, data) {
    var rowNum = 0
    var headerRow: Array<string> = []
    for (let row of data) {
        if (rowNum++ == 0) {
            headerRow = row;
            console.log("Header: " + headerRow);
            continue
        }
        else {
            console.log("\nRow: " + row);
            for (let i = 0; i < 14; i++) {
                console.log(headerRow[i] + " = " + row[i]);
            }
        }
        //console.log("Row: " + row);
        //for (let value of row) {
        //    console.log("Value: " + value)
        //}
    }
}) as csvParse.Parser;

const filePath = path.join(__dirname, '../../db/movement_addresses.csv');
fs.createReadStream(filePath).pipe(myParser);*/

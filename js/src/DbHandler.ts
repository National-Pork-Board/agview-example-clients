import parse, * as csvParse from 'csv-parse';
import * as fs from 'fs';
import * as path from 'path'

export async function getData(filePath: string): Promise<Map<string, any>[]> {
    var myParser: csvParse.Parser = parse({ delimiter: ',' }, function (err, data) {
    }) as csvParse.Parser;

    const fullFilePath = path.join(__dirname, filePath);
    let stream = fs.createReadStream(fullFilePath)
    stream.pipe(myParser);

    const chunks: Array<Array<string>> = [];
    const processedChunks: Array<Map<string, any>> = []

    return new Promise<Map<string, any>[]>(function (resolve, reject) {
        myParser.on("data", (chunk) => chunks.push(chunk))
        myParser.on("end", () => {
            var rowNum = 0
            var headerRow: Array<string> = []

            for (let row of chunks) {
                if (rowNum++ == 0) {
                    headerRow = row
                    continue
                }
                else {
                    let valueByFieldName: Map<string, any> = new Map()
                    for (let i = 0; i < headerRow.length; i++) {
                        valueByFieldName.set(headerRow[i], row[i])
                    }
                    processedChunks.push(valueByFieldName)
                }
            }
            resolve(processedChunks)
        })
        myParser.on("error", (err) => reject(err));
    })
}

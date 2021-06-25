import parse, * as csvParse from 'csv-parse';
import * as fs from 'fs';
import * as path from 'path'

interface CsvFileContent {
    header: string[],
    body: Map<string, any>[]
}

export async function getData(filePath: string): Promise<CsvFileContent> {
    const myParser: csvParse.Parser = parse({ delimiter: ',' }) as csvParse.Parser;

    const fullFilePath = path.join(__dirname, filePath);
    let stream = fs.createReadStream(fullFilePath)
    stream.pipe(myParser);

    const chunks: Array<Array<string>> = [];
    const processedChunks: Array<Map<string, any>> = []

    return new Promise<CsvFileContent>(function (resolve, reject) {
        myParser.on("data", (chunk) => chunks.push(chunk))
        myParser.on("end", () => {
            let rowNum = 0
            let headerRow: Array<string> = []

            for (let row of chunks) {
                if (rowNum++ == 0) {
                    headerRow = row
                }
                else {
                    let valueByFieldName: Map<string, any> = new Map()
                    for (let i = 0; i < headerRow.length; i++) {
                        valueByFieldName.set(headerRow[i], row[i])
                    }
                    processedChunks.push(valueByFieldName)
                }
            }

            resolve({ header: headerRow, body: processedChunks } as CsvFileContent)
        })
        myParser.on("error", (err) => reject(err));
    })
}

export async function getColumnNames(premFilePath: string): Promise<string[]> {
    return (await getData(premFilePath)).header
}

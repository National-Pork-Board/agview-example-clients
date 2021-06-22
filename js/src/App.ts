import { getData } from "./MovementDbHandler"
import { getAccessToken } from "./AccessTokenHandler"

let fileContentPromise = getData("../../db/movement.csv", "../../db/movement_addresses.csv")
fileContentPromise.then((fileContent) => {
    console.log(fileContent)
})

let accessTokenPromise = getAccessToken('http://localhost:8080', 'jjFuwiCo', 'Yxx6jqiEokX0zaYt1t9arCKKwDTrQjBz')

accessTokenPromise.then((data) => console.log(data))

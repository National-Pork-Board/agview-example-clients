import { getData } from "./MovementDbHandler"

let promise = getData("../../db/movement.csv", "../../db/movement_addresses.csv")
promise.then((fileContent) => {
    console.log(fileContent)
})

import { getMovementData, getMovementAddressesData } from "./MovementDbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'

let movementDataPromise = getMovementData("../../db/movement.csv")
let movementAddressesDataPromise = getMovementAddressesData("../../db/movement_addresses.csv")
movementDataPromise.then((fileContent) => {
    console.log(fileContent)
})
movementAddressesDataPromise.then((fileContent) => {
    console.log(fileContent)
})

const env = process.env
let accessTokenPromise = getAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)

accessTokenPromise.then((data) => console.log(data))

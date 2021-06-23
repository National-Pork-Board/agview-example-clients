import { getData } from "./MovementDbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import { Constants } from './Constants'

let fileContentPromise = getData("../../db/movement.csv", "../../db/movement_addresses.csv")
fileContentPromise.then((fileContent) => {
    console.log(fileContent)
})

const env = process.env
let accessTokenPromise = getAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)

accessTokenPromise.then((data) => console.log(data))

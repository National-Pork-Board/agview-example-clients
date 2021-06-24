import { getData } from "./DbHandler"
import { getAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'
import { createPrems } from './PremPostHandler'
import { createMovements } from './MovementPostHandler'

let dataPromise = getData("../../db/prem.csv")
let addressDataPromise = getData("../../db/prem_address.csv")
dataPromise.then((fileContent) => {
    //console.log(fileContent)
})
addressDataPromise.then((fileContent) => {
    //console.log(fileContent)
})

const env = process.env
let accessTokenPromise = getAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)

//accessTokenPromise.then((data) => console.log(data.access))

createPrems("../../db/prem.csv", "../../db/prem_address.csv")
createMovements("../../db/movement.csv", "../../db/movement_addresses.csv")

import { getData } from "./MovementDbHandler"
import { getAccessToken } from "./AccessTokenHandler"

let fileContentPromise = getData("../../db/movement.csv", "../../db/movement_addresses.csv")
fileContentPromise.then((fileContent) => {
    console.log(fileContent)
})

const env = process.env
let accessTokenPromise = getAccessToken(env.NPB_BASE_URL!, env.NPB_API_KEY!, env.NPB_API_SECRET!)

accessTokenPromise.then((data) => console.log(data))

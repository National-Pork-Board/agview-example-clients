import { getData, getColumnNames } from './DbHandler'
import { getNewAccessToken, getNonExpiredOrNewAccessToken } from './AccessTokenHandler'
import Constants from './Constants'
import { createPrems } from './PremPostHandler'
import { createAllMovements, createMovementsForTimestampRange } from './MovementPostHandler'

const ONE_SECOND = 1000

doIt()

async function doIt() {
    console.log('*********Handle Access Token*************************************************')
    let accessToken = await getNewAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)
    console.log(`New access token: ${JSON.stringify(accessToken)}\n`)
    await delay(ONE_SECOND)
    accessToken = await getNonExpiredOrNewAccessToken(
        Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET,
        accessToken, 10)
    console.log(`Same access token: ${JSON.stringify(accessToken)}\n`)
    await delay(ONE_SECOND)
    accessToken = await getNonExpiredOrNewAccessToken(
        Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET,
        accessToken, 100000)
    console.log(`New access token due to expiration: ${JSON.stringify(accessToken)}\n`)

    let premColumnNames = await getColumnNames('../../db/prem.csv')
    console.log(`Prem column names: ${Array.from(premColumnNames)}`)
    let premAddressColumnNames = await getColumnNames('../../db/prem_address.csv')
    console.log(`Prem Address column names: ${Array.from(premAddressColumnNames)}`)
    let movementColumnNames = await getColumnNames('../../db/movement.csv')
    console.log(`Movement column names: ${Array.from(movementColumnNames)}`)
    let movementAddressesColumnNames = await getColumnNames('../../db/movement_addresses.csv')
    console.log(`Movement Addresses column names: ${Array.from(movementAddressesColumnNames)}`)

    let premResponse = await createPrems('../../db/prem.csv', '../../db/prem_address.csv')
    console.log(`Created prems with status ${premResponse!.status}`)

    let allMovementResponse = await createAllMovements('../../db/movement.csv', '../../db/movement_addresses.csv')
    console.log(`Created all movements with status ${allMovementResponse!.status}`)

    let movementWithinTimestampResponse = await createMovementsForTimestampRange('../../db/movement.csv', '../../db/movement_addresses.csv', '2021-06-07T13:45', '2021-06-08T13:45')
    console.log(`Created movements within timestamp range with status ${movementWithinTimestampResponse!.status}`)
}

function delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms))
}
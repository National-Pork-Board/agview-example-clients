import { getColumnNames } from './DbHandler'
import { getNewAccessToken, getNonExpiredOrNewAccessToken } from './AccessTokenHandler'
import Constants from './Constants'
import { createPrems } from './PremPostHandler'
import { createAllMovements, createMovementsForTimestampRange } from './MovementPostHandler'

const ONE_SECOND = 1000
const PREM_FILE_PATH = '../../db/prem.csv'
const PREM_ADDRESS_FILE_PATH = '../../db/prem_address.csv'
const MOVEMENT_FILE_PATH = '../../db/movement.csv'
const MOVEMENT_ADDRESSES_FILE_PATH = '../../db/movement_addresses.csv'
const MOVEMENT_START_TIMESTAMP = '2021-06-07T13:45'
const MOVEMENT_END_TIMESTAMP = '2021-06-08T13:45'

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

    console.log('\n')
    console.log('*********Create Prems Using Multiple Data Sources*************************************************')
    console.log('Combining Prem data')
    console.log(`\t${await getColumnNames(PREM_FILE_PATH)}`)
    console.log('with Prem Address data')
    console.log(`\t${await getColumnNames(PREM_ADDRESS_FILE_PATH)}`)
    let premResponse = await createPrems(PREM_FILE_PATH, PREM_ADDRESS_FILE_PATH)
    console.log(`Created Prems: ${JSON.stringify(premResponse!.data, null, 1)}`)

    console.log('\n')
    console.log('*********Create Movements Using Multiple Data Sources*************************************************')
    console.log('Combining Movement data')
    console.log(`\t${await getColumnNames(MOVEMENT_FILE_PATH)}`)
    console.log('with Movement Addresses data')
    console.log(`\t${await getColumnNames(MOVEMENT_ADDRESSES_FILE_PATH)}`)
    let allMovementResponse = await createAllMovements(MOVEMENT_FILE_PATH, MOVEMENT_ADDRESSES_FILE_PATH)
    console.log(`Created Movements from entire data: ${JSON.stringify(allMovementResponse!.data, null, 1)}`)
    let movementForTimestampRangeResponse = await createMovementsForTimestampRange(MOVEMENT_FILE_PATH, MOVEMENT_ADDRESSES_FILE_PATH,
        MOVEMENT_START_TIMESTAMP, MOVEMENT_END_TIMESTAMP)
    console.log(`Created movements for date range ${MOVEMENT_START_TIMESTAMP} thru ${MOVEMENT_START_TIMESTAMP}: ${JSON.stringify(movementForTimestampRangeResponse!.data, null, 1)}`)
}

function delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms))
}
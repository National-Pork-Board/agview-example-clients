import { AxiosResponse } from 'axios';
import { createAllMovements, createMovementsForTimestampRange } from './MovementPostHandler'

const MOVEMENT_FILE_PATH = '../../db/movement.csv'
const MOVEMENT_ADDRESS_FILE_PATH = '../../db/movement_addresses.csv'

const START_DATE = "2021-06-07T13:45";
const END_DATE = "2021-06-08T13:45";

describe('MovementPostHandler', () => {
    it('creates all movements', async () => {
        let actual = (await createAllMovements(MOVEMENT_FILE_PATH, MOVEMENT_ADDRESS_FILE_PATH)) as AxiosResponse<Array<any>>

        expect(actual.data.length).toEqual(9)
    });

    it('creates movements for timestamp range', async () => {
        let actual = (await createMovementsForTimestampRange(MOVEMENT_FILE_PATH, MOVEMENT_ADDRESS_FILE_PATH, START_DATE, END_DATE)) as AxiosResponse<any>

        expect(actual.data.length).toEqual(7)
    });
})

import { getData } from './DbHandler'

const PREM_FILE_PATH = '../../db/prem.csv'

describe('DbHandler', () => {
    it('gets data', async () => {
        let actual = (await getData(PREM_FILE_PATH)).body

        expect(actual.length).toEqual(2)
        expect(actual[0].get('usda_pin')).not.toBeNull()
        expect(actual[1].get('usda_pin')).not.toBeNull()
    });

    it('gets column names', async () => {
        let actual = (await getData(PREM_FILE_PATH)).header

        expect(actual).toEqual(
            ["usda_pin", "prem_name", "site_capacity_number_animals", "ice_contact_email", "ice_contact_phone", 
            "location_type", "species", "site_capacity_number_barns", "number_of_animals_on_site"])
    });
})

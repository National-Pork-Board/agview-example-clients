import { AxiosResponse } from 'axios';
import { getNewAccessToken, getNonExpiredOrNewAccessToken } from './AccessTokenHandler'
import Constants from "./Constants";

describe('AccessTokenHandler', () => {
    it('gets new access token', async () => {
        let actual = await getNewAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)

        expect(actual.access).not.toBeNull()
        expect(actual.exp).toBeGreaterThan(0)
    });

    it('reuses existing non-expired access token', async () => {
        let existingAccessToken = await getNewAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)
        await delay(10)

        let actual = await getNonExpiredOrNewAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET,
            existingAccessToken, 10)

        expect(actual).toEqual(existingAccessToken)
    });

    function delay(ms: number) {
        return new Promise(resolve => setTimeout(resolve, ms))
    }

    it('gets new access token when existing one is outside of validity Range', async () => {
        let existingAccessToken = await getNewAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)
        await delay(10)

        let actual = await getNonExpiredOrNewAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET,
            existingAccessToken, 10000)

        expect(actual.exp).toBeGreaterThan(existingAccessToken.exp)
    });
})


using System;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using static Npb.Agview.Api.Example.Constants;

namespace Npb.Agview.Api.Example
{
    class Program
    {
        private const int OneSecond  = 1000;

        static async Task Main(string[] args)
        {
            var httpClient = new HttpClient();

            Console.WriteLine("*********Handle Access Token*************************************************");
            var accessTokenHandler = new AccessTokenHandler(httpClient, BaseUrl, ApiKey, ApiSecret);
            var accessToken = await accessTokenHandler.GetNewAccessToken();
            Console.WriteLine("New access token: " + accessToken + "\n");
            Thread.Sleep(OneSecond);
            Console.WriteLine("Same access token: " + await accessTokenHandler.GetNonExpiredOrNewAccessToken(accessToken, 10) + "\n");
            Thread.Sleep(OneSecond);
            Console.WriteLine("New access token due to expiration: " + await accessTokenHandler.GetNonExpiredOrNewAccessToken(accessToken, 100000) + "\n");

            Console.WriteLine();
            Console.WriteLine("*********Create Premises Using Multiple Data Sources*************************************************");
            var premiseDbHandler = new PremiseDbHandler(PremisesFilePath, PremiseAddressesFilePath);
            var premisePostHandler = new PremisePostHandler(httpClient, accessTokenHandler, BaseUrl, premiseDbHandler);
            Console.WriteLine("Combining Premise data");
            Console.WriteLine("\t" + string.Join(",", premiseDbHandler.GetPremiseColumnNames()));
            //Console.WriteLine("with PremiseAddress data");
            //Console.WriteLine("\t" + premiseDbHandler.getPremiseAddressColumnNames());
            //Collection<CreatedPremise> createdPremises = premisePostHandler.createPremises();
            //Console.WriteLine("Created premises: " + createdPremises);
        }
    }
}

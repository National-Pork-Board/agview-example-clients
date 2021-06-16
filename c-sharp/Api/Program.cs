using System;
using System.Collections.Generic;
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
            Console.WriteLine("\t" + string.Join(", ", premiseDbHandler.GetPremiseColumnNames()));
            Console.WriteLine("with PremiseAddress data");
            Console.WriteLine("\t" + string.Join(", ", premiseDbHandler.GetPremiseAddressColumnNames()));
            List<CreatedPremise> createdPremises = await premisePostHandler.CreatePremises();
            Console.WriteLine("Created premises: " + string.Join(", ", createdPremises));

            Console.WriteLine();
            Console.WriteLine("*********Create Movements Using Multiple Data Sources*************************************************");
            var movementDbHandler = new MovementDbHandler(MovementFilePath, MovementAddressesFilePath);
            var movementPostHandler = new MovementPostHandler(httpClient, accessTokenHandler, BaseUrl, movementDbHandler);
            Console.WriteLine("Combining Movement data");
            Console.WriteLine("\t" + string.Join(", ", movementDbHandler.GetMovementColumnNames()));
            Console.WriteLine("with MovementAddresses data");
            Console.WriteLine("\t" + string.Join(", ", movementDbHandler.GetMovementAddressesColumnNames()));
            Console.WriteLine("Created movements from the entire data: " + string.Join(", ", await movementPostHandler.CreateMovements()));
            var fromDate = "2021-06-07T00:00";
            var toDate = "2021-06-08T23:59";
            Console.WriteLine("Created movements for date range " + fromDate + " thru " + toDate + ": " + string.Join(", ", await movementPostHandler.CreateMovementsForDateRange(fromDate, toDate)));
        }
    }
}

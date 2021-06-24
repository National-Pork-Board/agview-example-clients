using System;
using System.Collections.Generic;
using System.IO;
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
            Console.WriteLine("*********Create Prems Using Multiple Data Sources*************************************************");
            var premDbHandler = new PremDbHandler(PremsFilePath, PremAddressesFilePath);
            var premPostHandler = new PremPostHandler(httpClient, accessTokenHandler, BaseUrl, premDbHandler);
            Console.WriteLine("Combining Prem data");
            Console.WriteLine("\t" + string.Join(", ", premDbHandler.GetPremColumnNames()));
            Console.WriteLine("with PremAddress data");
            Console.WriteLine("\t" + string.Join(", ", premDbHandler.GetPremAddressColumnNames()));
            List<CreatedPrem> createdPrems = await premPostHandler.CreatePrems();
            Console.WriteLine("Created prems: " + string.Join(", ", createdPrems));

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

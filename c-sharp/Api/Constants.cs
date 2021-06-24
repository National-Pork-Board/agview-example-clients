using System;
using System.IO;

namespace Npb.Agview.Api.Example
{
    public class Constants
    {
        public static readonly string BaseUrl = Environment.GetEnvironmentVariable("NPB_BASE_URL");
        public static readonly string ApiKey = Environment.GetEnvironmentVariable("NPB_API_KEY");
        public static readonly string ApiSecret = Environment.GetEnvironmentVariable("NPB_API_SECRET");

        private static readonly string dbDirectory =
             Path.GetFullPath(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "../../../../../db"));
        public static readonly string PremsFilePath = $"{dbDirectory}/prem.csv";
        public static readonly string PremAddressesFilePath = $"{dbDirectory}/prem_address.csv";
        public static readonly string MovementFilePath = $"{dbDirectory}/movement.csv";
        public static readonly string MovementAddressesFilePath = $"{dbDirectory}/movement_addresses.csv";
    }
}

using System;

namespace Npb.Agview.Api.Example
{
    public class Constants
    {

        public static readonly string BaseUrl = Environment.GetEnvironmentVariable("NPB_BASE_URL");
        public static readonly string ApiKey = Environment.GetEnvironmentVariable("NPB_API_KEY");
        public static readonly string ApiSecret = Environment.GetEnvironmentVariable("NPB_API_SECRET");

        public const string PremisesFilePath = "../../../../../db/premise.csv";
        public const string PremiseAddressesFilePath = "../../../../../db/premise_address.csv";
    }
}

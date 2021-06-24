using CsvHelper.Configuration.Attributes;

namespace Npb.Agview.Api.Example
{
    public class PremAddress
    {
        [Name("usda_pin")]
        public string UsdaPin { get; set; }

        [Name("street_address")]
        public string StreetAddress { get; set; }

        [Name("city")]
        public string City { get; set; }

        [Name("state")]
        public string State { get; set; }

        [Name("zip")]
        public string Zip { get; set; }
    }
}
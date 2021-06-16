using CsvHelper.Configuration.Attributes;

namespace Npb.Agview.Api.Example
{
    public class MovementAddresses
    {
        [Name("movement_id")]
        public int MovementId {get; set;}


        [Name("source")]
        public string Source {get; set;}

        [Name("source_latitude")]
        public string SourceLatitude {get; set;}

        [Name("source_longitude")]
        public string SourceLongitude {get; set;}

        [Name("source_street_address")]
        public string SourceStreetAddress {get; set;}

        [Name("source_city")]
        public string SourceCity {get; set;}

        [Name("source_state")]
        public string SourceState {get; set;}

        [Name("source_zip")]
        public string SourceZip {get; set;}


        [Name("destination")]
        public string Target {get; set;}

        [Name("destination_latitude")]
        public string TargetLatitude {get; set;}

        [Name("destination_longitude")]
        public string TargetLongitude {get; set;}

        [Name("destination_street_address")]
        public string TargetStreetAddress {get; set;}

        [Name("destination_city")]
        public string TargetCity {get; set;}

        [Name("destination_state")]
        public string TargetState {get; set;}

        [Name("destination_zip")]
        public string TargetZip {get; set;}
    }
}
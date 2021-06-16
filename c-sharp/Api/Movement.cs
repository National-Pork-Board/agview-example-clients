using CsvHelper.Configuration.Attributes;

namespace Npb.Agview.Api.Example
{
    public class Movement
    {
        [Name("movement_id")]  
        public int MovementId { get; set; }

        [Name("species")]
        public string Species { get; set; }

        [Name("number_in_shipment")]
        public int NumberInShipment { get; set; }

        [Name("movement_datetime")]
        public string MovementDatetime { get; set; }

        [Name("movement_type")]
        public string MovementType { get; set; }
    }
}
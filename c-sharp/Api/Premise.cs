using CsvHelper.Configuration.Attributes;

namespace Npb.Agview.Api.Example
{
    public class Premise {
        [Name("usda_pin")]
        public string UsdaPin { get; set; }

        [Name("prem_name")]
        public string PremName { get; set; }

        [Name("species")]
        public string Species { get; set; }

        [Name("ice_contact_phone")]
        public string IceContactPhone { get; set; }

        [Name("ice_contact_email")]
        public string IceContactEmail { get; set; }

        [Name("location_type")]
        public string LocationType { get; set; }

        [Name("site_capacity_number_barns")]
        public int SiteCapacityNumberBarns { get; set; }

        [Name("site_capacity_number_animals")]
        public int SiteCapacityNumberAnimals { get; set; }

        [Name("number_of_animals_on_site")]
        public int NumberOfAnimalsOnSite { get; set; }
    }
    
}
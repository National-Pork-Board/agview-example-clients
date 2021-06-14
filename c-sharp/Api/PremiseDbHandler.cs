using System.Collections.Generic;
using System.Globalization;
using System.IO;
using CsvHelper;

namespace Npb.Agview.Api.Example
{
    public class PremiseDbHandler
    {

        public string PremisesFilePath { get; }
        public string PremiseAddressesFilePath { get; }

        public PremiseDbHandler(string premisesFilePath, string premiseAddressesFilePath)
        {
            PremisesFilePath = premisesFilePath;
            PremiseAddressesFilePath = premiseAddressesFilePath;
        }

        public IEnumerable<Premise> GetPremisesToLoad()
        {
            using var reader = new StreamReader(PremisesFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);

            List<Premise> records = new();
            records.AddRange(csv.GetRecords<Premise>());

            return records;
        }

        public string[] GetPremisesColumnNames()
        {
            return GetColumnNames(PremisesFilePath);
        }

        private string[] GetColumnNames(string filePath)
        {
            using var reader = new StreamReader(filePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);
            csv.Read();
            csv.ReadHeader();

            return csv.HeaderRecord;
        }

        public IEnumerable<PremiseAddress> GetPremiseAddressesToLoad()
        {
            using var reader = new StreamReader(PremiseAddressesFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);

            List<PremiseAddress> records = new();
            records.AddRange(csv.GetRecords<PremiseAddress>());

            return records;
        }

        public string[] GetPremiseAddressesColumnNames()
        {

            return GetColumnNames(PremiseAddressesFilePath);
        }
    }
}

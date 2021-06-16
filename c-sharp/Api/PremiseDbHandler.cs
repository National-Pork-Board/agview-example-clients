using System.Collections.Generic;
using System.Globalization;
using System.IO;
using CsvHelper;

namespace Npb.Agview.Api.Example
{
    public class PremiseDbHandler
    {

        private readonly string _premisesFilePath;
        private readonly string _premiseAddressesFilePath;

        public PremiseDbHandler(string premisesFilePath, string premiseAddressesFilePath)
        {
            _premisesFilePath = premisesFilePath;
            _premiseAddressesFilePath = premiseAddressesFilePath;
        }

        public IEnumerable<Premise> GetPremisesToLoad()
        {
            using var reader = new StreamReader(_premisesFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);

            List<Premise> records = new();
            records.AddRange(csv.GetRecords<Premise>());

            return records;
        }

        public string[] GetPremiseColumnNames()
        {
            return GetColumnNames(_premisesFilePath);
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
            using var reader = new StreamReader(_premiseAddressesFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);

            List<PremiseAddress> records = new();
            records.AddRange(csv.GetRecords<PremiseAddress>());

            return records;
        }

        public string[] GetPremiseAddressColumnNames()
        {

            return GetColumnNames(_premiseAddressesFilePath);
        }
    }
}

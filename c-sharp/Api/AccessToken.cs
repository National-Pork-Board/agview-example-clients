namespace Npb.Agview.Api.Example
{
    public class AccessToken
    {
        public string Access { get; set; }
        public long Exp { get; set; }

        public override string ToString()
        {
            return $"Access Token = {Access}, Expiration Time (seconds) = {Exp}";
        }
    }
}
require 'google_places'

API_KEY = "AIzaSyAtimpMsj7gf5IgnH8y8P-pxpDmrrYI7C8"
class GooglePlacesWrapper
  def initialize
    @client = GooglePlaces::Client.new API_KEY
  end

  def search(lat, lng, name)
    results = @client.spots(lat, lng, name: name)
    return nil if results.nil? or results.empty?

    results.first
  end

  cache_method :search
end

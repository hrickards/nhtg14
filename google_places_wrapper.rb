require 'google_places'
require 'yaml'

CONFIG = YAML.load_file "config.yml"
API_KEY = CONFIG["GOOGLE_API_KEY"]
PHOTO_WIDTH = 800

class GooglePlacesWrapper
  def initialize
    @client = GooglePlaces::Client.new API_KEY
  end

  def search(lat, lng, name)
    name.gsub! " PH", ""

    results = @client.spots(lat, lng, name: name)
    return nil if results.nil? or results.empty?

    results.first
  end

  def parse_photos(photos)
    photos.map do |photo|
      photo.fetch_url(PHOTO_WIDTH)
    end
  end

  cache_method :search
end

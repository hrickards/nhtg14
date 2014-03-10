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
    name = name.split(" ").first

    results = @client.spots(lat, lng, name: name)
    return nil if results.nil? or results.empty?

    result = results.first
    result.reviews = fetch_reviews(result.reference)

    result
  end

  def parse_photos(photos)
    photos.map do |photo|
      photo.fetch_url(PHOTO_WIDTH)
    end
  end

  def fetch_reviews(reference)
    url = "https://maps.googleapis.com/maps/api/place/details/json?reference=#{reference}&sensor=true&key=#{API_KEY}"
    response = HTTParty.get url

    reviews = response["result"]["reviews"]
    return [] if reviews.nil? or reviews.empty?

    reviews.map! do |review|
      {
        rating: review["rating"],
        text: review["text"]
      }
    end

    reviews
  end

  cache_method :search
  cache_method :fetch_reviews
end

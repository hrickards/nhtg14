require 'bundler'
require 'httparty'

METRES_IN_A_MILE = 1609.34

# From ActiveSupport
class String
  def underscore
    self.gsub(/::/, '/').
    gsub(/([A-Z]+)([A-Z][a-z])/,'\1_\2').
    gsub(/([a-z\d])([A-Z])/,'\1_\2').
    tr("-", "_").
    downcase
  end
end

class FoodStandards
  include HTTParty

  BASE_URL = "http://api.ratings.food.gov.uk"
  HEADERS = {
    'x-api-version' => '2'
  }

  def initialize(params)
    @latitude = params[:lat]
    @longitude = params[:lng]
    self.search
  end

  def search
    endpoint =  BASE_URL + "/Establishments"
    options = {
      headers: HEADERS,
      query: {
        latitude: @latitude,
        longitude: @longitude,
        sortOptionKey: 'distance',
        maxDistanceLimit: 0.1,
        pageSize: 1
      }
    }
    results = self.class.get(endpoint, options)
    raise "No results".inspect unless results.include? "establishments"
    @establishments = results["establishments"]
    raise "No results".inspect if results.empty?
    puts @establishments.first.inspect if $DEBUG_API
  end

  def score
    @establishments.first["RatingValue"].to_i
  end

  def name
    @establishments.first["BusinessName"]
  end

  def scores
    data = @establishments.first["scores"].map do |k, v|
      [k.underscore.to_sym, v]
    end
    Hash[data]
  end

  # In metres
  # Returned from FSA in miles
  def distance
    (@establishments.first["Distance"].to_f * METRES_IN_A_MILE).to_i
  end

  def self.score(params)
    fs = FoodStandards.new params
    fs.score
  end
end

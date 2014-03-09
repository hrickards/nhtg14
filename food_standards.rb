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

  attr_accessor :establishments

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
    unless results.include? "establishments" and not results["establishments"].empty? and not results["establishments"].first.nil?
      @establishments = []
      return []
    end

    @establishments = results["establishments"]
    puts @establishments.first.inspect if $DEBUG_API
  end
  cache_method :search

  def score
    return "" if @establishments.empty?
    @establishments.first["RatingValue"].to_i
  end

  def name
    return "" if @establishments.empty?
    @establishments.first["BusinessName"]
  end

  def scores
    return "" if @establishments.empty?
    data = @establishments.first["scores"].map do |k, v|
      [k.underscore.to_sym, v]
    end
    Hash[data]
  end

  # In metres
  # Returned from FSA in miles
  def distance
    return "" if @establishments.empty?
    (@establishments.first["Distance"].to_f * METRES_IN_A_MILE).to_i
  end

  def location
    return "" if @establishments.empty?
    geocode = @establishments.first["geocode"].map do |k, v|
      [k.underscore.to_sym, v.to_f]
    end
    Hash[geocode]
  end

  def self.score(params)
    fs = FoodStandards.new params
    fs.score
  end
end

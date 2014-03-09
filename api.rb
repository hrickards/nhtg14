require 'bundler'
require 'grape'

require 'cache_method'
require 'redis'
CacheMethod.config.storage = Redis.new

require './food_standards.rb'
require './google_places_wrapper.rb'

$DEBUG_API = true

$google_places = GooglePlacesWrapper.new

class Api < Grape::API
  format :json
  prefix "api"

  resource :ratings do
    desc "Get score for an establishment with a certain lat and lng."
    params do
      optional :lat, type: String, desc: "Latitude"
      optional :lng, type: String, desc: "Longitude"
    end
    get do
      puts params.inspect
      return {} if params[:lat].nil? or params[:lng].nil?

      fs = FoodStandards.new({lat:params[:lat], lng: params[:lng]})
      fs_hash = {
        score: fs.score,
        name: fs.name,
        scores: fs.scores,
        distance: fs.distance,
        location: fs.location
      }

      puts "got fsa data"
    
      gp = $google_places.search(fs.location[:latitude], fs.location[:longitude], fs.name)
      if gp.nil?
        gp_hash = {}
      else
        gp_hash = {
          types: gp.types,
          reviews: gp.reviews,
          photos: gp.photos
        }
      end

      fs_hash.merge gp_hash
    end
  end
end

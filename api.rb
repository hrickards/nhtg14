require 'bundler'
require 'grape'

require './food_standards.rb'

$DEBUG_API = true

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
      {
        score: fs.score,
        name: fs.name,
        scores: fs.scores,
        distance: fs.distance
      }
    end
  end
end

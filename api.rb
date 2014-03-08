require 'bundler'
require 'grape'

require './food_standards.rb'

$DEBUG_API = true

class Api < Grape::API
  format :json

  resource :establishments do
    desc "Get score for an establishment with a certain lat and lng."
    params do
      requires :lat, type: String, desc: "Latitude"
      requires :lng, type: String, desc: "Longitude"
    end
    get do
      {
        score: FoodStandards.score({lat:params[:lat], lng: params[:lng]})
      }
    end
  end
end

# The range store.
require 'java'
java_import 'org.apollo.game.model.inter.store.Shop'

# Define our values.
id = 3
name = "Ranging Shop"
items = { 
  # item id => item amount,
  1065 => 100, 1099 => 100, 1135 => 100, 2487 => 100, 2493 => 100, 2499 => 100, 2489 => 100, 2495 => 100, 2501 => 100, 2491 => 100, 2497 => 100, 2503 => 100, 884 => 10000, 886 => 10000, 888 => 10000, 890 => 10000, 892 => 10000, 9236 => 10000, 9237 => 10000, 9238 => 10000, 9239 => 10000, 9240 => 10000, 9241 => 10000, 9242 => 10000, 9243 => 10000, 9244 => 10000, 9245 => 10000, 9185 => 100, 4214 => 100, 861 => 100
}
type = Shop::ShopType::UNLIMITED_BUY_ONLY

# Ship out the shop to the world.
shop = appendShop(Shops.new(id, name, items, type))
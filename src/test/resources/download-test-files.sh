# cards
curl https://api.scryfall.com/cards/random -o random-card.json;
curl https://api.scryfall.com/cards/named?exact=Lightnin+Bolt -o lightnin-bolt.json
curl https://api.scryfall.com/cards/481e9f7a-ecbb-4697-9c2e-c30967015eff -o spanish-wheel-of-fate.json
curl https://api.scryfall.com/cards/5646ea19-0025-4f88-ad22-36968a1d3b89 -o pony.json
curl https://api.scryfall.com/cards/e8ca02a0-5acf-4d89-847b-bad0d7560682 -o accessories-to-murder.json
curl https://api.scryfall.com/cards/aa4ca825-7e52-4f26-9ab8-b68a7e294182 -o ashling-the-pilgrim-avatar.json
curl https://api.scryfall.com/cards/11bf83bb-c95b-4b4f-9a56-ce7a1816307a -o delver-of-secrets.json
curl https://api.scryfall.com/cards/c1a316a5-04a3-4128-8d62-58192e2265a5 -o absorb.json
curl https://api.scryfall.com/cards/ab68bd00-7151-4a6b-ad98-134ca02d7d59 -o sliver-queen.json
curl https://api.scryfall.com/cards/f8772631-d4a1-440d-ac89-ac6659bdc073 -o unstable-forest.json
curl https://api.scryfall.com/cards/ab68bd00-7151-4a6b-ad98-134ca02d7d59 -o sliver-queen.json
curl https://api.scryfall.com/cards/ab68bd00-7151-4a6b-ad98-134ca02d7d59 -o sliver-queen.json
curl https://api.scryfall.com/cards/ab68bd00-7151-4a6b-ad98-134ca02d7d59 -o sliver-queen.json

# sets
curl https://api.scryfall.com/sets/a4a0db50-8826-4e73-833c-3fd934375f96 -o aer.json
curl https://api.scryfall.com/sets/cb890bc8-ec73-449e-9be0-46891f39eea1 -o thou.json
curl https://api.scryfall.com/sets/63c89a12-d115-4084-a4af-fceef40ca02f -o v17.json
curl https://api.scryfall.com/sets/a944551a-73fa-41cd-9159-e8d0e4674403 -o vma.json

# symbols
curl https://api.scryfall.com/symbology/ -o symbology.json
curl 'https://api.scryfall.com/symbology/parse-mana?cost=2g2' -o symbology-2g2.json
curl 'https://api.scryfall.com/symbology/parse-mana?cost=XURW' -o symbology-XURW.json
curl 'https://api.scryfall.com/symbology/parse-mana?cost=½CC' -o symbology-halfcc.json

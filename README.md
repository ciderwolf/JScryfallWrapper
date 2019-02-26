# JScryfallWrapper

A Java wrapper to [Scryfall's Magic: the Gathering API]("https://scryfall.com/docs/api").

## Usage

### Cards

See `Card.namedExactly` and `Card.namedFuzzy` to retrieve a card based on its name.

`Card.fromMtgoId` returns a card which corresponds to the card with the supplied ID on Magic Online.

`Set.getCards` returns a list of cards located in that set object.

#### Images

Information about a card's imagery is stored in an `Images` object, which is either on the card itself, or on its faces. `Card` also has convenience methods `getImage` and `getImageURI`, which will return the card's image or URI regardless of whether or not the card has multiple faces. These default to the value of the front face if the card has multiple faces.

The static method `Card.getImage` will return an image for the card with the specified name without having to first retrieve all other card data. This only returns the front face of the requested card. 

#### Text

`Card.getText` returns Scryfall's text representation of the card object. The text representation for [Delver of Secrets](https://scryfall.com/card/isd/51/delver-of-secrets-insectile-aberration) is the following:

    Delver of Secrets {U}
    Creature — Human Wizard
    At the beginning of your upkeep, look at the top card of your library. You may reveal that card. If an instant or sorcery card is revealed this way, transform Delver of Secrets.
    1/1
    ----
    Insectile Aberration
    Color Indicator: Blue
    Creature — Human Insect
    Flying
    3/2

### Sets

See `Set.fromCode` to retrieve a set based on its set code

### Lists

`List.fromURL` will return a list from the specified URL.

Retrieve the contents of a list by using `getContents`, which allows for the retrieval of data of any type, or `getCards` which will return the contents as an array of Cards.

### Catalogs

`Catalog.fromIdentifier` returns the Catalog of the specified name.

### Bulk Data
`BulkData.getBulkData` returns an array of `BulkData` objects which represent all of Scryfall's different Bulk Data stores. 

## Error Handling
If a request to Scryfall's API returns an error, the corresponding object's `isError()` method will return true, and its `getError()` method wil return a `ScryfallError` object containing details about what went wrong. 
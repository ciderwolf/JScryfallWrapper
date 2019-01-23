# JScryfallWrapper

A Java wrapper to [Scryfall's Magic: the Gathering API]("https://scryfall.com/docs/api").

## Usage

### Cards

See <code>Card.namedExactly</code> and <code>Card.namedFuzzy</code> to retrieve a card based on its name.

<code>Card.fromMtgoId</code> returns a card which corresponds to the card with the supplied ID on Magic Online.

<code>Set.getCards</code> returns a list of cards located in that set object.

#### Images

Information about a card's imagery is stored in an <code>Images</code> object, which is either on the card itself, or on its faces. <code>Card</code> also has convenience methods <code>getImage</code> and <code>getImageURI</code>, which will return the card's image or URI regardless of whether or not the card has multiple faces. These default to the value of the front face if the card has multiple faces.

The static method <code>Card.getImage</code> will return an image for the card with the specified name without having to first retrieve all other card data. This only returns the front face of the requested card. 

#### Text

<code>Card.getText</code> returns Scryfall's text representation of the card object. The text representation for [Delver of Secrets](https://scryfall.com/card/isd/51/delver-of-secrets-insectile-aberration) is the following:

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

See <code>Set.fromCode</code> to retrieve a set based on its set code

### Lists

<code>List.fromURL</code> will return a list from the specified URL.

Retrieve the contents of a list by using <code>getContents</code>, which allows for the retrieval of data of any type, or <code>getCards</code> which will return the contents as an array of Cards.

### Catalogs

<code>Catalog.fromIdentifier</code> returns the Catalog of the specified name. 
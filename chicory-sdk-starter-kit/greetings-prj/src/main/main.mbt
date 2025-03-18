pub fn hello_world() ->  Int {
    @host.output_string("👋 Hello, world! 🌍")
    0 // success
}

pub fn hello() -> Int {
  let jsonString = @host.input_string()

  let parsedJson = match @json.parse?(jsonString) {
    Ok(jsonValue) => jsonValue
    Err(e) => "unable to parse JSON: \{e}"
  } 

    // Accessing fields in the parsed JSON
  let firstName = match parsedJson {
      { "firstName": String(n), .. } => n 
      _ => jsonString
  }

    let lastName = match parsedJson {
      { "lastName": String(n), .. } => n 
      _ => jsonString
  }

    let message = "👋 Hello, " + firstName + " " + lastName + " 🙂!"
    @host.output_string(message)
    0 // success
}

pub fn vulcan_salute() -> Int {

  let jsonString = @host.input_string()

  let parsedJson = match @json.parse?(jsonString) {
    Ok(jsonValue) => jsonValue
    Err(e) => "unable to parse JSON: \{e}"
  }

    // Accessing fields in the parsed JSON
  let firstName = match parsedJson {
      { "firstName": String(n), .. } => n 
      _ => jsonString
  }

    let lastName = match parsedJson {
      { "lastName": String(n), .. } => n 
      _ => jsonString
  }

    let message = "🖖 Live long and prosper, " + firstName + " " + lastName + " 🚀!"
    @host.output_string(message)
    0 // success
}


fn main {
  
}

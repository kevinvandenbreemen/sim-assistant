# sim-assistant

# Coding Rules

## Interactors

### Concurrency
These Components that Interact with Persistence/web APIs etc should be written so that they can be 
tested synchronusly.  Ensuring these operations are performed asynchronously should be the
responsibility of client code (for example, controllers or presenters).
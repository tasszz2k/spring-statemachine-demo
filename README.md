# Spring state machine demo

## Introduction
- This is a simple demo of Spring state machine. It is a simple state machine that can be used to manage the state of an object.
- States:
  + DRAFT
  + FA_REVIEW
  + REJECTED
  + APPROVED
  + DISTRIBUTING
  + IN_USE_REVIEW
  + IN_USE_APPROVED
  + ENDED
- Events:
  + CREATE,
  + SUBMIT,
  + APPROVE,
  + REJECT,
  + EDIT,
  + DISTRIBUTE,
  + USE,
  + TERMINATE,
  + END
  
  ![campaign-approval.png](docs/campaign-approval.png)

  ![img.png](img.png)
## APIs
### 1. Create a new campaign
![img.png](docs/img.png)

### 2. Get a campaign by id
![img_1.png](docs/img_1.png)

### 3. Trigger a campaign event (edit, approve, reject)
![img_2.png](docs/img_2.png)
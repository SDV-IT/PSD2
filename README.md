# PSD2
Berlin Group XS2A Implementation for the OAuth2 approach

In this github archive you will find our view of the Berlin Group Interpretation (V 0.9) of PSD2 XS2A.

We choose the XAuth2 approach.

Currently we work on the new V 1.0 Specifiction of the Berlin Group.

The folder xs2a contains a ASPSP (Account Servicing Payment Service Provider) implementation 
The code include a mock implement of a core banking system. The implementation is targeting Java Enterprise Edition 7 with Java 8.

Under the tpp folder we provide a showcase implementation. Our main focus is, to give a better understanding 
on the workflows and how this API should be used. Therefore it’s technical focused. For how to use, please view our clips on this side.

By using OAuth an identity provider is mandatory. To setup a local Identity Provider is not an easy 
job so we decided to provide a simple mock implementation. This mock is (as our backend implementation) runnable on a standard Java EE 7 stack.

Security aspects like check correctness of an access token is not part of this code. We are solving this kind of issues in 
components like firewalls, API-gateways, … like most of our competitor.

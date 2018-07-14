import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'channels.dart';

const String ACTIONS = "actions";

void main() {
  runApp(new MaterialApp(
    home: new HelpScreenWidget(),
  ));
}

class HelpScreenWidget extends StatefulWidget {
  @override
  _State createState() => new _State();
}

class _State extends State<HelpScreenWidget> {

  BasicMessageChannel<String> _actionsChannel = new BasicMessageChannel(
      ACTIONS, StringCodec());

  @override
  Widget build(BuildContext context) {
    return new Container(
      padding: new EdgeInsets.all(32.0),
      child: new Column(
        children: <Widget>[
          new Text('Simming Catalogue and Dictator', style: TextStyle(
              color: Colors.lightGreen,
              fontWeight: FontWeight.bold
          ),),
          new Card(
            child: new Column(
              children: <Widget>[
                new Text("By Kevin VanDenBreemen")
              ],
            ),
          ),
          new FlatButton(onPressed: doClose, child: new Text("OK"))
        ],
      ),
    );
  }

  void doClose() {
    _actionsChannel.send(RETURN_TO_MAIN);
  }
}
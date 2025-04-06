// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';

function loadWebView(webviewPath: string) {
	const readPath = path.resolve(__dirname, webviewPath);
	return fs.readFileSync(readPath, { encoding: 'utf-8'});
}

const webviewPanel = vscode.window.createWebviewPanel(
    'webviewId',
    'WebView Title',
    vscode.ViewColumn.One,
);

export function activate(context: vscode.ExtensionContext) {
	const disposable = vscode.commands.registerCommand('practice.showWebview', () => {
	  const panel = vscode.window.createWebviewPanel(
		'practiceWebview',
		'Webview Practice',
		vscode.ViewColumn.One,
		{
		  enableScripts: true
		}
	  );
  
	  const htmlPath = path.join(context.extensionPath, 'out', 'webview.html');
	  const htmlContent = fs.readFileSync(htmlPath, 'utf8');
	  panel.webview.html = htmlContent;
	});
  
	context.subscriptions.push(disposable);
  }
  
  export function deactivate() {}
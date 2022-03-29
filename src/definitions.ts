export interface AdvanceBrowserPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
	integrations: [
		starlight({
			title: 'Plugins Runtime',
			social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/Quurz/Plugins' }],
			sidebar: [
				{
					label: 'Getting Started',
					items: [
						{ label: 'Introduction', slug: 'guides/introduction' },
						{ label: 'Installation', slug: 'guides/installation' },
					],
				},
				{
					label: 'Usage',
					items: [
						{ label: 'Basic Usage', slug: 'guides/usage' },
						{ label: 'Creating a Plugin', slug: 'guides/creating-a-plugin' },
					],
				},
				{
					label: 'Reference',
					items: [
						{ label: 'Plugin Metadata', slug: 'reference/plugin-metadata' },
						{ label: 'API Reference', slug: 'reference/api' },
					],
				},
			],
		}),
	],
});

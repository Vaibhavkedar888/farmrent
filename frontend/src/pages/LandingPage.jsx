import { motion } from 'framer-motion';
import { Tractor, Sprout, Users, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useLanguage } from '../context/useLanguage';
import { useRef } from 'react';

const LandingPage = () => {
    const { t } = useLanguage();
    const videoRef = useRef(null);

    return (
        <div className="min-h-screen bg-white">
            {/* Hero Section */}
            <div className="relative overflow-hidden min-h-[90vh] flex items-center justify-center">
                <img
                    src="bd.jpg"
                    alt="Farming Background"
                    className="absolute top-0 left-0 w-full h-full object-cover z-0 brightness-[0.65]"
                />
                <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
                    <div className="text-center">
                        <motion.h1
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            className="text-5xl font-extrabold text-white tracking-tight sm:text-6xl"
                        >
                            {t('heroTitle').split(' ').slice(0, -2).join(' ')} <span className="text-primary-500">{t('heroTitle').split(' ').slice(-2).join(' ')}</span>
                        </motion.h1>
                        <motion.p
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ delay: 0.2 }}
                            className="mt-6 max-w-2xl mx-auto text-xl text-gray-200 font-light drop-shadow-md"
                        >
                            {t('heroSubtitle')}
                        </motion.p>
                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ delay: 0.4 }}
                            className="mt-10 flex flex-col sm:flex-row justify-center gap-4 items-center"
                        >
                            <Link to="/login" className="px-8 py-3 rounded-full bg-primary-600 text-white font-semibold hover:bg-primary-700 transition shadow-lg flex items-center gap-2">
                                {t('getStarted')} <ArrowRight size={20} />
                            </Link>
                            <Link to="/about" className="px-8 py-3 rounded-full bg-white text-primary-600 font-semibold border border-primary-200 hover:bg-primary-50 transition shadow-sm">
                                {t('learnMore')}
                            </Link>
                        </motion.div>
                    </div>
                </div>
            </div>

            {/* Features */}
            <div className="py-20 bg-white">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-12">
                        <FeatureCard
                            icon={<Tractor size={40} className="text-primary-600" />}
                            title={t('equipment')}
                            description="From tractors to harvesters, find everything you need for mechanised farming."
                        />
                        <FeatureCard
                            icon={<Users size={40} className="text-primary-600" />}
                            title="Direct Farmer Connection"
                            description="Rent directly from equipment owners. No middlemen, transparent pricing."
                        />
                        <FeatureCard
                            icon={<Sprout size={40} className="text-primary-600" />}
                            title={t('schemes')}
                            description="Access modern technology and government support without heavy investment."
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

const FeatureCard = ({ icon, title, description }) => (
    <motion.div
        whileHover={{ y: -5 }}
        className="p-8 bg-gray-50 rounded-2xl hover:shadow-xl transition duration-300 border border-gray-100"
    >
        <div className="w-16 h-16 bg-white rounded-xl flex items-center justify-center shadow-sm mb-6">
            {icon}
        </div>
        <h3 className="text-xl font-bold text-gray-900 mb-3">{title}</h3>
        <p className="text-gray-600 leading-relaxed">{description}</p>
    </motion.div>
);

export default LandingPage;
